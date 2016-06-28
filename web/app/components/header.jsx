export default class Header extends React.Component {
    render() {
        return <header>
            <div className="label-container">
                <h1>Memories</h1>

                <SiteMenu location={this.props.location} />
            </div>
        </header>;
    }
}
