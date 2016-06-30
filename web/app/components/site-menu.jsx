export default class SiteMenu extends React.Component {
    constructor() {
        super();

        this.state = {
            user: CurrentUserStore.value('user')
        };

        this.unwatch = CurrentUserStore.watch('user', (user) => this.setState({user: user}));
    }

    componentWillUnmount() {
        this.unwatch();
    }

    render() {
        return <ul className="site-menu">
            <li><Link to="/">Home</Link></li>

            {this.state.user ? this.renderLoggedIn() : this.renderLoggedOut()}

            <li><a href="">About</a></li>
        </ul>;
    }

    renderLoggedIn() {
        return [
            <li>Hello {this.state.user.email}</li>
        ];
    }

    renderLoggedOut() {
        return [
            <li><a href="">Register</a></li>,
            <li>
                <Link to={{
                    pathname: 'login',
                    state: {modal: true, returnTo: this.props.location.pathname}
                }}>Login</Link>
            </li>
        ];
    }
}
