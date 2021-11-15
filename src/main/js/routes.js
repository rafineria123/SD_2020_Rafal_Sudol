import React from 'react';
import { Route, IndexRoute } from 'react-router';

import App from '../js/app';


export default (
    <Route path="/" component={App}>
        <IndexRoute component={App} />
        <Route path="page:a(.*)" component={App} />
        <Route path="/page:a(.*)" component={App} />
    </Route>

);