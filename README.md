- **CI**  
[![sync-pr](https://github.com/begyyal/trading_calculator/actions/workflows/sync-pr.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/sync-pr.yml)
[![push-stg](https://github.com/begyyal/trading_calculator/actions/workflows/push-stg.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/push-stg.yml)  

- **Development complements**  
[![create-feature](https://github.com/begyyal/trading_calculator/actions/workflows/create-feature.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/create-feature.yml)
[![delete-feature](https://github.com/begyyal/trading_calculator/actions/workflows/delete-feature.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/delete-feature.yml)  

- **Others**  
[![push-tags](https://github.com/begyyal/trading_calculator/actions/workflows/push-tags.yml/badge.svg)](https://github.com/begyyal/trading_calculator/actions/workflows/push-tagse.yml)  

# Overview

This is a trading tool that calculates profit and loss, original indicatores, and shows some market data.  
It's wip currently, first edition's plan is followings.  

## Conditions
 - java
 - java FX
 - Store data in file directly.  
 - Market data is quoted by web scraping.  

## Goal
 - Calculate PL(profit/loss) of input positions and lots at stock index, fx, commodities.
 - PL is displayed in JPY.
 - The calculation is triggered pushing button (and considering automatic calculation at fixed interval in a polling thread).

## Prospects and plan
 - Displaying currency should be optional.
 - The calculation could be automatic due to linking MQL, but it narrows preconditions of using this tool.  
 - Add to original indicator.
