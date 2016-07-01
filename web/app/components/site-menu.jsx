export default class SiteMenu extends StoreAwareComponent {
    constructor() {
        super({
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
            <li>Hello {this.state.user.email}</li>,
            <li><a onClick={this.logout.bind(this)}>Logout</a></li>
        ];
    }

    logout() {
        CurrentUserStore.logout();
    }

    renderLoggedOut() {
        return [
            <li>
                <Link to={{
                    pathname: 'register',
                    state: {modal: true, returnTo: this.props.location.pathname}
                }}>Register</Link>
            </li>,
            <li>
                <Link to={{
                    pathname: 'login',
                    state: {modal: true, returnTo: this.props.location.pathname}
                }}>Login</Link>
            </li>
        ];
    }
}
