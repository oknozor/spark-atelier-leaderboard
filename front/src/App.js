import './App.css';
import React from 'react';
import ReactNotifications from 'react-notifications-component';
import banner from './assets/banner.png'
import Notifier from './components/Notifier';
import Leaderboard from "./components/Leaderboard";
import { Animated } from "react-animated-css";


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
        <Animated animationIn="bounceInLeft" animationOut="fadeOut" isVisible={true}>
          <header><img src={banner}></img> </header>
        </Animated>
        <Leaderboard/>
        <Notifier />
        <ReactNotifications />
      </div>
    );
  }
}

export default App;
