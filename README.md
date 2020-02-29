# react-native-appruve-mobile-sdk

## Getting started

`$ npm install react-native-appruve-mobile-sdk --save`

## Usage
```javascript
import AppruveMobileSdk from 'react-native-appruve-mobile-sdk';

// Start the verification activity
AppruveMobileSdk.startVerification(
    'YOUR API TOKEN',
    '#00693C' // statusBarColor,
    '#0F5738' // toolbarBackgroundColor,
    '#292f33' // headerTextColor,
    '#292f33' // subHeaderTextColor,
    '#00693C' // textLabelColor,
    '#292f33' // normalTextColor,
    '#75AF96' // iconColor,
    '#292f33' // iconTextColor,
    '#75AF96' // navigationButtonBackgroundColor,
    true // isGhanaEnabled,
    true // isNigeriaEnabled,
    true // isKenyaEnabled,
)
    .then(isVerified => { // isVerified is a Boolean
        console.log(isVerified);
    })
    .catch(e => {
        console.log(e);
    });
};
```
