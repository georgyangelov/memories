export default class SiteMenu extends StoreAwareComponent {
    constructor(props) {
        super(props, {
            user: 'CurrentUserStore.user'
        });
    }

    render() {
        return <ul className="site-menu">
            <li><Link to="/">Home</Link></li>

            {this.state.user ? this.renderLoggedIn() : this.renderLoggedOut()}
        </ul>;
    }

    renderLoggedIn() {
        return [
            <li key="hello">Hello {this.state.user.email}</li>,
            <li key="logout"><a onClick={this.logout.bind(this)}>Logout</a></li>
        ];
    }

    logout() {
        CurrentUserStore.logout();
    }

    renderLoggedOut() {
        return [
            <li key="register">
                <Link to={{
                    pathname: 'register',
                    state: {modal: true, returnTo: this.props.location.pathname}
                }}>Register</Link>
            </li>,
            <li key="login">
                <Link to={{
                    pathname: 'login',
                    state: {modal: true, returnTo: this.props.location.pathname}
                }}>Login</Link>
            </li>
        ];
    }
}
