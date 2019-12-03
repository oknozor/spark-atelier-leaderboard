import React from 'react';

class LeaderboardModule extends React.Component {


    render() {
        console.log(this.props.avatar)
        return (
            <li>
                <span className="list_num">{this.props.rank}</span>
                <img src={this.props.avatar.avatar} />
                <h2>{this.props.team.name}<span className="number">{this.props.team.score}</span></h2>
            </li>
        );
    }
}

export default LeaderboardModule;
