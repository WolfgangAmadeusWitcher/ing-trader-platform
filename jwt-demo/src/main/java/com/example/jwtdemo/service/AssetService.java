package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.AssetNotFoundException;
import com.example.jwtdemo.model.Asset;
import com.example.jwtdemo.model.Order;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.model.UserRole;
import com.example.jwtdemo.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    public List<Asset> getAssets(User currentUser) {
        if(currentUser.getUserRole() == UserRole.ADMIN){
            return assetRepository.findAll();
        }
        return assetRepository.findByOwner_Id(currentUser.getId());
    }

    public Asset findAssetForUser(User currentUser, String assetName) {
        return assetRepository.findByOwner_IdAndAssetName(currentUser.getId(), assetName).orElseThrow(
                () -> new AssetNotFoundException(assetName)
        );
    }

    public Asset findOrCreateAssetForUser(User currentUser, Order order) {
        return assetRepository.findByOwner_IdAndAssetName(currentUser.getId(), order.getAssetName()).orElseGet(
                () -> {
                    Asset newUserAsset = new Asset();
                    newUserAsset.setAssetName(order.getAssetName());
                    newUserAsset.setSize(0);
                    newUserAsset.setUsableSize(0);
                    newUserAsset.setOwner(currentUser);
                    return assetRepository.save(newUserAsset);
                });
    }

    public Asset updateAsset(Asset asset) {
        return assetRepository.save(asset);
    }
}
