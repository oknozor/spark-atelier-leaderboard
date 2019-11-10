import React from 'react';
import TweetComponent from './TweetComponent';


class TweetsComponent extends React.Component {

    render() {
        return (
            <table>
                <tr>
                    <th>User</th>
                    <th>Content</th>
                </tr>
                { this.props.tweets.map(tweet => <TweetComponent tweet={tweet}/>) }
            </table>
        )
    }
}

export default TweetsComponent