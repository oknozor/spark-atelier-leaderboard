const palette = ['#33b2df', '#546E7A', '#d4526e', '#13d8aa', '#A5978B', '#2b908f', '#f9a3a4', '#90ee7e',
    '#f48024', '#69d2e7'
]

class Team {
    constructor(id, name) {
        this.id = id
        this.name = name;
        this.stepCount = 0;
        this.color = palette[Math.floor(Math.random() * palette.length)];
    }

    static ToTeam(team) {
        let toTeam = new Team(team.id, team.name)
        toTeam.stepCount = team.stepCount
        toTeam.color = palette[Math.floor(Math.random() * palette.length)];
        return toTeam
    }
}

export default Team