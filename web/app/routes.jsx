import {render} from 'react-dom';
import {Router, Route, IndexRoute, Link, hashHistory} from 'react-router';

import App from './app.jsx';

global.history = hashHistory;

render((
    <Router history={hashHistory}>
        <Route path="/" component={App}>
            <IndexRoute component={views.Index} />

            <Route path="/login" component={views.Login}/>
        </Route>
    </Router>
), document.getElementById('app'))
