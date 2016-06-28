export default class Login extends React.Component {
    render() {
        return <div className="login-view">
            <h1>Login</h1>
            <form className="padding">
                <fieldset className="form-group">
                    <label for="email">Email address</label>
                    <input type="email" className="form-control" id="email" placeholder="Enter email" />
                    <small className="text-muted">We'll never share your email with anyone else.</small>
                </fieldset>

                <fieldset className="form-group">
                    <label for="password">Password</label>
                    <input type="password" className="form-control" id="password" placeholder="Password" />
                </fieldset>

                <button type="submit" className="btn btn-primary btn-block">Submit</button>
            </form>
        </div>;
    }
}
