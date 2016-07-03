export default class Index extends StoreAwareComponent {
    constructor(props) {
        super(props, {
            user: 'CurrentUserStore.user',
            images: 'ImageStore.images'
        });

        ImageStore.reload();
    }

    render() {
        return <div>
            <SearchBox onSubmit={this.onSearch.bind(this)} />

            {this.state.user && <ImageUploader />}

            <ImageGrid images={this.state.images} />
        </div>;
    }

    onSearch(query) {
        if (query) {
            appHistory.push(`/search/${encodeURIComponent(query)}`);
        }
    }
}
