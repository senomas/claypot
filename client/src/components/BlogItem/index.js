import React, { PropTypes, Component } from 'react';
import { connect } from 'react-redux';
import classnames from 'classnames';

import './style.css';

export default class BlogItem extends Component {

   render() {
      const { className, id, title, path } = this.props;
      return (
         <div className={classnames('BlogItem', className)}>
            {id} : {title} : {path}
         </div>
      );
   }
}
