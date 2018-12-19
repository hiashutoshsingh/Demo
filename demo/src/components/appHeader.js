
import React, {Component} from 'react';
import {Platform, Text,Image} from 'react-native';
import {Left,Button,Header,Icon,Right,Title,Body} from 'native-base';

type Props = {};
export default class AppHeader extends Component<Props> {
  render() {
    return (
        <Header>
          
          <Body>
            <Image source={require('../img/images.png')} style={{ width: 390, height: 50}}/>
          </Body>
          
        </Header>
      
    );
  }
}

module.export = AppHeader;