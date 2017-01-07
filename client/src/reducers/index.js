import { combineReducers } from 'redux';

import { BLOG_LIST } from '../actions/';

const rootReducer = combineReducers({
   blog: (state, action) => {
      if (!state) state = {};
      switch (action.type) {
      case BLOG_LIST:
         // console.log('BLOG_LIST '+JSON.stringify(action.payload));
         return action.payload;
      default:
         return state;
      }
   }
});

export default rootReducer;
