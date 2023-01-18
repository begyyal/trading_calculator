- **CI**  
[![sync-pr](https://github.com/begyyal/trading_calculator/actions/workflows/sync-pr.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/sync-pr.yml)
[![push-stg](https://github.com/begyyal/trading_calculator/actions/workflows/push-stg.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/push-stg.yml)  

- **Development complements**  
[![create-feature](https://github.com/begyyal/trading_calculator/actions/workflows/create-feature.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/create-feature.yml)
[![delete-feature](https://github.com/begyyal/trading_calculator/actions/workflows/delete-feature.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/delete-feature.yml)  

- **Others**  
[![push-tags](https://github.com/begyyal/trading_calculator/actions/workflows/push-tags.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/push-tagse.yml)  

# Overview

This is a trading tool that calculates risk tolerances, original indicatores, and shows some market data.  
It's wip currently, first edition's plan is followings.  

## Conditions
 - java
 - java FX
 - Store data in file directly.  
 - Market data from [TradingEconomics](http://docs.tradingeconomics.com/) is quoted.  

## Goal
 - Calculate risk tolerance of input positions and lots at stock index, fx, commodities.
 - The risk tolerance is displayed in JPY.
 - The calculation is not automatic, triggered pushing button.

## Prospects and plan
 - Displaying currency should be optional.
 - The calculation could be automatic due to linking MQL, but it narrows preconditions of using this tool.  
 - Add to original indicator.
