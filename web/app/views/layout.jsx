export default class Layout extends React.Component {
    render() {
        return <div className="site">
            <Header location={this.props.location} />

            <div className="view-content">{this.props.children}</div>
        </div>;
    }
}
