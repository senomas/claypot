// src/components/App/index.js
import React, { PropTypes, Component } from 'react';
import classnames from 'classnames';
import axios from 'axios';

import logo from './logo.svg';
import './style.css';

class App extends Component {
  // static propTypes = {}
  // static defaultProps = {}
  // state = {}

   componentDidMount() {
      console.log('componentDidMount');
      axios.post('/api/v1.0/blog/list', {
      }).then(function (response) {
         console.log('RESP: '+JSON.stringify(response.data.content));
      }).catch(function (error) {
         console.log('ERR: '+error);
      });
      console.log('componentDidMount done');
   }

   render() {
      const { className, ...props } = this.props;
      return (
         <div className={classnames('App', className)} {...props}>
            <div className="App-header">
               <img src={logo} className="App-logo" alt="logo" />
               <h2>Welcome to React</h2>
            </div>
            <p className="App-intro">
               To get started, edit <code>src/App.js</code> and save to reload.
            </p>
         </div>
      );
   }
}

export default App;
