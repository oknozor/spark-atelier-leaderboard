import React from 'react';

class TweetComponent extends React.Component {
    render() {
        return (
            <tr>
                <td>{this.props.tweet.user.name}</td>
                <td>{this.props.tweet.text}</td>
            </tr>
        )
    }
}

export default TweetComponent