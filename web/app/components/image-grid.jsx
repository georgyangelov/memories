var maxRowWidth = window.innerWidth - 1,
    preferredRowHeight = 300,
    maxRowHeight = 500;

export default class ImageGrid extends React.Component {
    render() {
        return <div className="image-grid">
            {this.rows().map(this.renderRow.bind(this))}
        </div>;
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
                <ImageThumbnail image={image} />
            </div>;
        });

        return <div className="image-grid-row">{rowElements}</div>;
    }
}
