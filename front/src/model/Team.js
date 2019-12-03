class Team {
    constructor(team) {
        this.id = team.id
        this.name = team.name;
        this.stepCount = team.currenStepId;
        this.score = team.score === undefined ? 0 : team.score;
    }
}

export default Team
