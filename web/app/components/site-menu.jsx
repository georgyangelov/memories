export default class SiteMenu extends React.Component {
    render() {
        return <ul className="site-menu">
            <li><Link to="/">Home</Link></li>
            <li><a href="">Register</a></li>
            <li>
                <Link to={{
                    pathname: 'login',
                    state: {modal: true, returnTo: this.props.location.pathname}
                }}>Login</Link>
            </li>
            <li><a href="">About</a></li>
        </ul>;
    }
}
