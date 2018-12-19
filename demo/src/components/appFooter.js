
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';
import {Left,Right,Button,Footer,FooterTab,Icon} from 'native-base';

type Props = {}; 
export default class AppFooter extends Component<Props> {
  render() {
    return (
        
        <Footer>
          <FooterTab>
            <Button vertical>
              <Icon name="apps" />
              <Text>Apps</Text>
            </Button>
            <Button vertical>
              <Icon name="camera" />
              <Text>Camera</Text>
            </Button>
            <Button vertical active>
              <Icon active name="navigate" />
              <Text>Navigate</Text>
            </Button>
          </FooterTab>
        </Footer>
      
    );
  }
}

module.export = AppFooter;