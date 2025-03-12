package com.example.jwtdemo.exception;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String assetName) {
        super(String.format("Asset not found with name: %s", assetName));
    }
}
