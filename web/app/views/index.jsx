export default class Index extends StoreAwareComponent {
    constructor(...args) {
        super({
            user: 'CurrentUserStore.user',
            images: 'ImageStore.images'
        }, ...args);

        ImageStore.reload();
    }

    render() {
        return <div>
            <SearchBox onSubmit={this.onSearch.bind(this)} />

            {this.state.user && <ImageUploader />}

            <ImageGrid images={this.state.images} onShowMap={this.onShowMap.bind(this)} />
        </div>;
    }

    onSearch(query) {
        if (query) {
            this.context.router.push(`/search/${encodeURIComponent(query)}`);
        }
    }

    onShowMap(image) {
        this.context.router.push({
            pathname: `/images/${encodeURIComponent(image.id)}/map`,
            state: {
                modal: true,
                returnTo: this.props.location.pathname
            }
        });
    }
}

Index.contextTypes = {
    router: React.PropTypes.object.isRequired
};
