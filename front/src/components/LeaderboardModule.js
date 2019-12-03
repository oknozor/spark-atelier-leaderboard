import React from 'react';

class LeaderboardModule extends React.Component {


    render() {
        return (
            <li>
                <span className="list_num Score Score--bounce">{this.props.rank}</span>
                <img src="https://s3.amazonaws.com/uifaces/faces/twitter/jsa/128.jpg" />
                <h2>{this.props.team.name}<span className="number">{this.props.team.score}</span></h2>
            </li>
        );
    }
}

export default LeaderboardModule;
