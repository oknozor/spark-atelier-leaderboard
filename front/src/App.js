import './App.css';

import React from 'react';
import TeamComponent from './components/TeamComponent.js';


class App extends React.Component {

  render() {
    return (
      <div className="App">
        <TeamComponent/>
      </div>
    );
  }
}

export default App;
