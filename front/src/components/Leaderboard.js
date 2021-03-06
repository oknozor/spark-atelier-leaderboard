import React from 'react';
import Team from "../model/Team";
import TeamAvatar from "../model/TeamAvatar";
import EventBus from "vertx3-eventbus-client";
import Notify from "./Notifier";
import LeaderboardModule from "./LeaderboardModule";
import { Animated } from "react-animated-css";

class Leaderboard extends React.Component {
    constructor(props) {
        super(props)
        this.eventbus = null;
        this.state = {
            teams: [],
            avatars: [], // (teamid, avatar)
            changed: -1 // (teamId, bool)
        }
        this.addTeam = this.addTeam.bind(this);
        this.getTeam = this.getTeam.bind(this);
        this.completeStep = this.completeStep.bind(this);
        this.fetchTeamOnStart = this.fetchTeamOnStart.bind(this);
    }

    addTeam(team) {
        let maybeNewTeam = this.getTeam(team.id)

        // is maybeNewTeam is undefine we can process and register the new team
        if (maybeNewTeam === undefined) {
            let newTeam = new Team(team);
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
        let index = this.state.teams.indexOf(teamToUpdate);
        console.log(stepCompletion)
        if (index !== -1) {
            let newState = this.state.teams;
            newState[index] = new Team(stepCompletion)
            this.setState({ teams: newState, changed: stepCompletion.id })
            console.log(newState)
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
        fetch("http://spark-leaderboard-backend.hoohoot.org/teams", {
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "auth": "xFeGdHjhqsixiZBUntXvSTGGMfsIzOcshZIVxWVoxnjWyzDXhTxHyiwbnoZnTVttFgJFCglnmHYyLhWSverHWCPMGSsumXPkyuWV"
            },
            credentials: 'include',
            method: "GET",
        }).then((response) => response.json())
            .then(teams => {
                console.log(teams)
                this.setState({
                    teams: teams.map(team => new Team(team)),
                    avatars: teams.map(team => new TeamAvatar(team.id))
                })
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

        this.eventbus = new EventBus('http://spark-leaderboard-backend.hoohoot.org/eventbus', options)
        this.eventbus.enableReconnect(true)

        this.eventbus.onopen = () => {
            console.log("connected to vertx event bus!")

            this.eventbus.registerHandler('team.register', (error, message) => {
                console.log(message)
                this.addTeam(message.body)
            });

            this.eventbus.registerHandler('step.completion', (error, message) => {
                console.info(message)
                this.completeStep(message.body)
            });

            this.eventbus.registerHandler('twitter.info', (error, message) => {
                console.log(message.body)
                console.log(error)
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
                            .sort((team1, team2) => team2.score - team1.score)
                            .map((team, idx) => <LeaderboardModule
                                team={team} rank={idx + 1}
                                avatar={this.state.avatars.find(avatar => avatar.id === team.id)} />)}
                    </ul>
                </div>
            </div>
        );
    }
}

export default Leaderboard;
