import React from 'react';
import { FixedSizeList as List } from 'react-window';
import '../ListStyle.css';


const Row = ({ data, index, style }) => (
    <div className={index % 2 ? 'ListItemOdd' : 'ListItemEven'} style={style}>
        {data[index].user.name} : {data[index].text}
    </div>
);


class TweetsComponent extends React.Component {


    render() {
        let length = this.props.tweets.length;
        console.log(length)
        return (
            <List
                itemData={this.props.tweets}
                className="List"
                height={200}
                itemCount={length}
                itemSize={25}
                width={1000}
                rowHeight={25}
            >
                {Row}
            </List>
        )
    }
}
export default TweetsComponent