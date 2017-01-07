// src/index.js
import React from 'react';
import ReactDOM from 'react-dom';
import { browserHistory } from 'react-router';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import reduxThunk from 'redux-thunk';
import Routes from './routes';
import reducer from './reducers';

import './index.css';

const store = applyMiddleware(reduxThunk)(createStore)(reducer);

ReactDOM.render(
   <Provider store={store}>
      <Routes history={browserHistory} store={store}/>
   </Provider>,
   document.getElementById('root')
);
