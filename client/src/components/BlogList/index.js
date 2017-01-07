import React, { PropTypes, Component } from 'react';
import { connect } from 'react-redux';
import classnames from 'classnames';

import BlogItem from '../BlogItem';

import { loadBlogs } from '../../actions';

import './style.css';

class BlogList extends Component {

   componentDidMount() {
      this.props.loadBlogs();
   }

   render() {
      const { className, blog } = this.props;
      if (!!blog && !!blog.data) {
         console.log('BLOG '+JSON.stringify(blog));
         var items = [];
         blog.data.content.forEach((item, index) => {
            console.log('BLOG ITEM '+index+' '+JSON.stringify(item));
            items.push(
               <BlogItem { ...item } />
            );
         });
         return (
            <ol>{ items }</ol>
         );
      }
      return (
         <div className={classnames('BlogList', className)}>
            <br/>
            NO BLOGS
         </div>
      );
   }
}

function mapStateToProps(state) {
  return {
    blog: state.blog
  };
}

export default connect(mapStateToProps, { loadBlogs }) (BlogList);
