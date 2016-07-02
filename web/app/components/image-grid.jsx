var maxRowWidth = window.innerWidth - 1,
    preferredRowHeight = 300,
    maxRowHeight = 500;

export default class ImageGrid extends React.Component {
    constructor() {
        super();

        this.state = {
            currentImage: 0,
            lightboxOpen: false
        };
    }

    render() {
        var imagesForLightbox = this.props.images.map(image => {
            return {src: image.imageUrl};
        });

        return <div className="image-grid">
            {this.rows().map(this.renderRow.bind(this))}

            <Lightbox images={imagesForLightbox}
                      isOpen={this.state.lightboxOpen}
                      currentImage={this.state.currentImage}
                      onClickPrev={this.previousImage.bind(this)}
                      onClickNext={this.nextImage.bind(this)}
                      onClose={() => this.setState({lightboxOpen: false})} />
        </div>;
    }

    viewImage(image) {
        var imageIndex = _.findIndex(this.props.images, currentImage => {
            return currentImage.id == image.id;
        });

        this.setState({
            currentImage: imageIndex,
            lightboxOpen: true
        });
    }

    previousImage() {
        this.setState({
            currentImage: this.state.currentImage > 0 ?
                          this.state.currentImage - 1 :
                          this.props.images.length - 1
        });
    }

    nextImage() {
        this.setState({
            currentImage: (this.state.currentImage + 1) % this.props.images.length
        });
    }

    rows() {
        var images = this.props.images,
            row = [],
            rowWidth = 0,
            rows = [];

        images.forEach(image => {
            var scaledImageWidth = image.width * (preferredRowHeight / image.height),
                image = _.extend({}, image, {scaledWidth: scaledImageWidth});

            row.push(image);
            rowWidth += image.scaledWidth;

            if (rowWidth > maxRowWidth) {
                rows.push({images: row, width: rowWidth});
                row = [];
                rowWidth = 0;
            }
        });

        if (row.length > 0) {
            rows.push({images: row, width: rowWidth});
        }

        return rows;
    }

    renderRow(row) {
        var newRowHeight = preferredRowHeight * (maxRowWidth / row.width);

        if (newRowHeight > maxRowHeight) {
            newRowHeight = preferredRowHeight;
        }

        var rowElements = row.images.map(image => {
            var style = {
                width: image.scaledWidth * (newRowHeight / preferredRowHeight),
                height: newRowHeight
            };

            return <div className="image-grid-image" style={style}>
                <ImageThumbnail image={image} onView={this.viewImage.bind(this, image)} />
            </div>;
        });

        return <div className="image-grid-row">{rowElements}</div>;
    }
}
