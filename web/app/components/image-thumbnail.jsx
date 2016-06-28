export default class ImageThumbnail extends React.Component {
    render() {
        var style = {
            backgroundImage: `url(${this.props.image.url})`
        };

        return <div className="image-thumbnail" style={style}>
            <div className="controls">
                <h2>Image name</h2>
                <h3>#tag1 #tag2 #tag3 #tag4</h3>
            </div>
        </div>;
    }
}
