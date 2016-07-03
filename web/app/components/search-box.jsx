export default class SearchBox extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            query: this.props.initialQuery || ''
        };
    }

    componentDidMount() {
        ReactDOM.findDOMNode(this.refs.input).focus();
    }

    render() {
        return <form className="search-box" onSubmit={this.onSubmit.bind(this)}>
            <input type="text" className="form-control" placeholder="Search for tags" ref="input"
                   onChange={(e) => this.setState({query: e.target.value})} value={this.state.query} />
        </form>;
    }

    onSubmit(event) {
        event.preventDefault();
        this.props.onSubmit(this.state.query);
    }
}
