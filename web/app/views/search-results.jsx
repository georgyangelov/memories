export default class SearchResults extends StoreAwareComponent {
    constructor(props) {
        super(props, {});

        this.state = {images: []};
    }

    componentDidMount() {
        this.load(this.props.params.query);
    }

    componentWillReceiveProps(newProps) {
        if (this.props.params.query != newProps.params.query) {
            this.load(newProps.params.query);
        }
    }

    load(query) {
        Image.search(query, (error, images) => {
            if (error) {
                console.error('Cannot load images', error);
                return;
            }

            this.setState({images: images});
        });
    }

    render() {
        return <div>
            <SearchBox initialQuery={this.props.params.query} onSubmit={this.onSearch.bind(this)} />

            <ImageGrid images={this.state.images} />
        </div>;
    }

    onSearch(query) {
        if (query) {
            appHistory.push(`/search/${encodeURIComponent(query)}`);
        } else {
            appHistory.push('/');
        }
    }
}
