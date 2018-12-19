
import React, {Component} from 'react';
import {Platform,Text, View,Image} from 'react-native';
import {Content,Card,CardItem,Body,Left,Thumbnail,Button,Icon} from 'native-base';
import TimeAgo from 'react-native-timeago'; 
import {GetImage,ContentSnippet} from '../helpers/helpers';
 
type Props = {};
export default class AppBodyData extends Component<Props> {

	render()
		{ 
			let articles = this.props.data.map(function(articleData,index){
				return(
					  <Card>
			            <CardItem>
			              <Left>
               	 <Thumbnail source={require('../img/Ashutosh_akgec.jpg')} />
                	<Body>
                  		<Text>{articleData.source.name}</Text>
                	</Body>
              			</Left>
			            </CardItem>

			            <CardItem>
			           <Image source={{uri: GetImage(articleData.urlToImage)}}
			           style={{ width: 390, height: 250}} />
			             </CardItem>

			<CardItem>  
              <Body>
                <Text>
                		{ContentSnippet(articleData.description)}
                </Text>
              </Body>
            </CardItem>
            <CardItem>
              <Left>
                <Body>
                <Text><TimeAgo time={articleData.publishedAt}/></Text>
                </Body>
              </Left>
              </CardItem>
			          </Card>
				)
			});


			return(
			<Content>
       	  		{articles}
        	</Content>
        );
		}
}

module.export = AppBodyData;