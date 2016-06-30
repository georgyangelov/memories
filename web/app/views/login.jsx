export default class Login extends React.Component {
    constructor() {
        super();

        this.state = {
            email: '',
            password: '',
            errors: []
        };
    }

    render() {
        return <div className="login-view">
            <h1>Login</h1>
            <form className="padding" onSubmit={this.register.bind(this)}>
                <fieldset className="form-group">
                    <label for="email">Email address</label>
                    <input type="text" className="form-control" id="email" placeholder="Enter email"
                           onChange={(event) => this.setState({email: event.target.value})} />
                    <small className="text-muted">We'll never share your email with anyone else.</small>
                </fieldset>

                <fieldset className="form-group">
                    <label for="password">Password</label>
                    <input type="password" className="form-control" id="password" placeholder="Password"
                           onChange={(event) => this.setState({password: event.target.value})}/>
                </fieldset>

                {this.renderErrors(this.state.errors || [])}

                <button type="submit" className="btn btn-primary btn-block">Submit</button>
            </form>
        </div>;
    }

    renderErrors(errors) {
        return errors.map((error) => {
            return <div className="error">{error}</div>;
        });
    }

    register(event) {
        event.preventDefault();

        this.setState({errors: []});

        User.create(this.state.email, this.state.password, (error, data) => {
            if (error) {
                this.setState({errors: error.messages});
                return;
            }

            User.auth(this.state.email, this.state.password, (error, data) => {
                if (error) {
                    this.setState({errors: error.messages});
                    return;
                }

                console.log('logged in');
            });
        });
    }
}
