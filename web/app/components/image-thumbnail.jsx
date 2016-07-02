export default class ImageThumbnail extends StoreAwareComponent {
    constructor() {
        super({
            currentUser: 'CurrentUserStore.user'
        });
    }

    render() {
        var image = this.props.image,
            style = {backgroundImage: `url(${image.imageUrl})`};

        return <div className="image-thumbnail" style={style}>
            <div className="controls">
                {image.name && <h2>{image.name}</h2>}
                <h3>by {image.user.name}</h3>

                {this.renderActions()}
            </div>
        </div>;
    }

    renderActions() {
        return <div className="actions">
            <button onClick={this.props.onView}
                    className="btn btn-primary btn-sm">View</button>

            {this.isImageAdmin() && <button onClick={this.deleteImage.bind(this)}
                                            className="btn btn-danger btn-sm">Delete</button>}
        </div>;
    }

    isImageAdmin() {
        return this.props.image.user.id == this.state.currentUser.id;
    }

    deleteImage() {
        if (!window.confirm('Are you sure you want to delete this image?')) {
            return;
        }

        Image.delete(this.props.image.id, (error) => {
            if (error) {
                console.error('Could not delete image', error);
                return;
            }

            ImageStore.reload();
        });
    }
}
