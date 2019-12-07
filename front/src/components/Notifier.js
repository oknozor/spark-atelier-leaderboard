import React from 'react'
import { store } from 'react-notifications-component';
import 'react-notifications-component/dist/theme.css';
import 'animate.css';

function Notify(content) {
  store.addNotification({
    title: 'Shame',
    message: String(content),
    type: 'warning',                         // 'default', 'success', 'info', 'warning'
    container: 'bottom-right',                // where to position the notifications
    animationIn: ["animated", "fadeIn"],     // animate.css classes that's applied
    animationOut: ["animated", "fadeOut"],   // animate.css classes that's applied
    dismiss: {
      duration: 10000
    }
  })

  return <span></span>
}

export default Notify
