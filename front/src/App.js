import './App.css';
import React from 'react';
import ReactNotifications from 'react-notifications-component';

import Notifier from './components/Notifier';
import TeamComponent from './components/TeamComponent.js';

class App extends React.Component {
  constructor() {
    super()

    this.state = {
      notification: "",
    }
  }

  render() {
    return (
      <div className="App">
        <TeamComponent />
        <Notifier />
        <ReactNotifications />
      </div>
    );
  }
}

export default App;
