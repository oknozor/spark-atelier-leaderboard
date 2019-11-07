class Team {
    constructor(id, name) {
        this.id = id
        this.name = name;
        this.stepCount = 0;
    }

    static ToTeam(team) {
        let toTeam = new Team(team.id, team.name)
        toTeam.stepCount = team.stepCount
        return toTeam
    }
}

export default Team
