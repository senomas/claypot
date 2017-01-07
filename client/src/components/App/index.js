import React, { PropTypes, Component } from 'react';
import { connect } from 'react-redux';
import classnames from 'classnames';

import BlogList from "../BlogList";

import logo from './logo.svg';
import './style.css';

export default class App extends Component {

   render() {
      const { className } = this.props;
      return (
         <div className={classnames('App', className)}>
            <div className="App-header">
               <img src={logo} className="App-logo" alt="logo" />
               <h2>Welcome to React</h2>
            </div>
            <BlogList />
         </div>
      );
   }
}
