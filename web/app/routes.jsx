import {render} from 'react-dom';
import {Router, Route, IndexRoute, Link, hashHistory} from 'react-router';

import App from './app.jsx';

global.appHistory = hashHistory;

render((
    <Router history={appHistory}>
        <Route path="/" component={App}>
            <IndexRoute component={views.Index} />

            <Route path="/login" component={views.Login}/>
            <Route path="/register" component={views.Register}/>
        </Route>
    </Router>
), document.getElementById('app'))
