export default class Index extends StoreAwareComponent {
    constructor() {
        super({
            user: 'CurrentUserStore.user',
            images: 'ImageStore.images'
        });

        ImageStore.reload();
    }

    render() {
        return <div>
            {this.state.user && <ImageUploader />}

            <ImageGrid images={this.state.images} />
        </div>;
    }
}
