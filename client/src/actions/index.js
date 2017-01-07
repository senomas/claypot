import axios from 'axios';

export const BLOG_LIST = 'BLOG_LIST';

export function loadBlogs() {
   return function(dispatch) {
      axios.get('/api/v1.0/blog/list').then(response => {
         dispatch({
            type: BLOG_LIST,
            payload: response
         });
      }).catch((error) => {
         console.log(error);
      });
   };
}
