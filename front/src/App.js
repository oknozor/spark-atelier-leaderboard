import './App.css';
import React from 'react';
import ReactNotifications from 'react-notifications-component';

import Notifier from './components/Notifier';
import Leaderboard from "./components/Leaderboard";

class App extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      notification: "",
    }
  }

  render() {
    return (
      <div className="App">
        {/*<TeamComponent />*/}
        <Leaderboard/>
        <Notifier />
        <ReactNotifications />
      </div>
    );
  }
}

export default App;
