export default class Register extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            email: '',
            password: '',
            errors: []
        };
    }

    render() {
        return <div className="login-view">
            <h1>Register</h1>

            <form className="padding" onSubmit={this.register.bind(this)}>
                <fieldset className="form-group">
                    <label for="name">Name</label>
                    <input type="text" className="form-control" id="name" placeholder="Enter your name"
                           onChange={(event) => this.setState({name: event.target.value})} />
                </fieldset>

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

    register(event) {
        event.preventDefault();

        this.setState({errors: []});

        User.create(this.state.name, this.state.email, this.state.password, (error, data) => {
            if (error) {
                this.setState({errors: error.messages});
                return;
            }

            User.auth(this.state.email, this.state.password, (error, data) => {
                if (error) {
                    this.setState({errors: error.messages});
                    return;
                }

                CurrentUserStore.login(data.user, data.accessToken);
                this.props.close();
            });
        });
    }
}
