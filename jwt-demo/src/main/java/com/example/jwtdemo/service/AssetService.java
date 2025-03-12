package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.AssetNotFoundException;
import com.example.jwtdemo.model.Asset;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.model.UserRole;
import com.example.jwtdemo.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Asset findOrCreateAssetForUser(User currentUser, String assetName) {
        return assetRepository.findByOwner_IdAndAssetName(currentUser.getId(), assetName).orElseGet(
                () -> {
                    Asset newUserAsset = new Asset();
                    newUserAsset.setAssetName(assetName);
                    newUserAsset.setSize(new BigDecimal("0"));
                    newUserAsset.setUsableSize(new BigDecimal("0"));
                    newUserAsset.setOwner(currentUser);
                    return assetRepository.save(newUserAsset);
                });
    }

    public Asset updateAsset(Asset asset) {
        return assetRepository.save(asset);
    }
}
