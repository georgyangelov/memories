export default class Login extends React.Component {
    constructor(...args) {
        super(...args);

        this.state = {
            email: '',
            password: '',
            errors: []
        };
    }

    render() {
        return <div className="login-view">
            <h1>Login</h1>

            <form className="padding" onSubmit={this.login.bind(this)}>
                <fieldset className="form-group">
                    <input type="text" className="form-control" id="email" placeholder="Enter email"
                           onChange={(event) => this.setState({email: event.target.value})} />
                </fieldset>

                <fieldset className="form-group">
                    <input type="password" className="form-control" id="password" placeholder="Password"
                           onChange={(event) => this.setState({password: event.target.value})}/>
                </fieldset>

                {this.renderErrors(this.state.errors || [])}

                <button type="submit" className="btn btn-primary btn-block">Submit</button>
                <button type="button" onClick={this.props.close}
                        className="btn btn-block cancel-btn">Close</button>
            </form>
        </div>;
    }

    renderErrors(errors) {
        return errors.map((error) => {
            return <div className="error">{error}</div>;
        });
    }

    login(event) {
        event.preventDefault();

        this.setState({errors: []});

        User.auth(this.state.email, this.state.password, (error, data) => {
            if (error) {
                this.setState({errors: error.messages});
                return;
            }

            CurrentUserStore.login(data.user, data.accessToken);
            this.props.close();
        });
    }
}
