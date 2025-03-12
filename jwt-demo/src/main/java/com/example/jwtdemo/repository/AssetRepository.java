package com.example.jwtdemo.repository;

import com.example.jwtdemo.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByOwner_Id(long ownerId);
    Optional<Asset> findByOwner_IdAndAssetName(long ownerId, String assetName);
}
