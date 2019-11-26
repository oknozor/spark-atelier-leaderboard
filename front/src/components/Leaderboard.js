import React from 'react';
import Team from "../model/Team";
import EventBus from "vertx3-eventbus-client";
import Notify from "./Notifier";
import LeaderboardModule from "./LeaderboardModule";

class Leaderboard extends React.Component {
    constructor(props) {
        super(props)
        this.eventbus = null;
        this.state = {
            teams: []
        }
        this.addTeam = this.addTeam.bind(this);
        this.getTeam = this.getTeam.bind(this);
        this.completeStep = this.completeStep.bind(this);
        this.fetchTeamOnStart = this.fetchTeamOnStart.bind(this);
    }

    addTeam(team) {
        console.log(team)
        let maybeNewTeam = this.getTeam(team.id)

        // is maybeNewTeam is undefine we can process and register the new team
        if (maybeNewTeam === undefined) {
            let newTeam = new Team(team.id, team.name);
            this.setState({
                teams: [...this.state.teams, newTeam]
            })
            // otherwise the team is registered already
        } else {
            console.error("team [" + maybeNewTeam.id + ", " + maybeNewTeam.name + "] already registered")
        }
    }

    // replace a team with the one sent on the eventbus
    completeStep(stepCompletion) {
        let teamToUpdate = this.getTeam(stepCompletion.id)
        console.log("preparing to update" + teamToUpdate)
        let index = this.state.teams.indexOf(teamToUpdate);

        if (index !== -1) {
            let newState = this.state.teams;
            teamToUpdate.stepCount = stepCompletion.stepCount
            newState[index] = teamToUpdate
            this.setState({ teams: newState })
        } else {
            console.error("No such team!")
        }
    }

    // returns a team or undefined
    getTeam(id) {
        return this.state.teams
            .find(team => team.id === id)
    }

    // Get the current state on start
    fetchTeamOnStart() {
        fetch("http://localhost:8080/teams", {
            headers: { "Accept": "application/json", "Content-Type": "application/json" },
            method: "GET",
        }).then((response) => response.json())
            .then(teams => {
                let teamsWithColor = teams.map(team => Team.ToTeam(team))
                this.setState({
                    teams: teamsWithColor
                })
                console.log(teamsWithColor)
            })
            .catch((err) => console.error(err));
    }


    // Connect to vertx event bus on component mount
    componentDidMount() {
        this.fetchTeamOnStart()

        var options = {
            vertxbus_reconnect_attempts_max: Infinity, // Max reconnect attempts
            vertxbus_reconnect_delay_min: 1000, // Initial delay (in ms) before first reconnect attempt
            vertxbus_reconnect_delay_max: 5000, // Max delay (in ms) between reconnect attempts
            vertxbus_reconnect_exponent: 2, // Exponential backoff factor
            vertxbus_randomization_factor: 0.5 // Randomization factor between 0 and 1
        };

        this.eventbus = new EventBus('http://localhost:8080/eventbus', options)
        this.eventbus.enableReconnect(true)

        this.eventbus.onopen = () => {
            console.log("connected to vertx event bus!")

            this.eventbus.registerHandler('team.register', (error, message) => {
                this.addTeam(message.body)
            });

            this.eventbus.registerHandler('step.completion', (error, message) => {
                this.completeStep(message.body)
            });

            this.eventbus.registerHandler('step.failure', (error, stepFailure) => {
                let message = "Team " + stepFailure.body.name + " failed current step, SHAME! "
                Notify(message)
            });
        }
    }

    render() {
        return (
            <div className="table">
                <div className="table-cell">
                    <ul className="leader">
                        <div id="decoration"></div>
                        <div id="decoration2"></div>
                        <div id="decoration3"></div>
                        {this.state.teams
                            .sort((team1, team2) => team2.stepCount - team1.stepCount)
                            .map(team => <LeaderboardModule team={team}/>)}
                    </ul>
                </div>
            </div>
        );
    }
}

export default Leaderboard;
