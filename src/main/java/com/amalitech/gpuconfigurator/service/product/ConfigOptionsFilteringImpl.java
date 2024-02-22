package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfigOptionsFilteringImpl implements ConfigOptionsFiltering {

    private final ProductRepository productRepository;
    private final CategoryConfigServiceImpl categoryConfigService;


    public List<UUID> getProductTypes(String productType) {
        List<Product> products = productRepository.findAll();
        List<UUID> productList = new ArrayList<>();
        Result result = new Result(products, productList);

        if (productType != null && !productType.isEmpty()) {
            String[] productTypeList = productType.split(",");
            for (Product product : result.products()) {
                CategoryConfigResponseDto configs = categoryConfigService.getCategoryConfigByCategory(String.valueOf(product.getCategory().getId()));
                for (String type : productTypeList) {
                    if (configs.options().containsKey(type.trim())) {
                        result.productList().add(product.getId());
                        break;
                    }
                }
            }
        }
        return result.productList();
    }

    private record Result(List<Product> products, List<UUID> productList) {
    }

    public List<UUID> getProcessor(String processor) {
        List<Product> products = productRepository.findAll();
        List<UUID> processorList = new ArrayList<>();

        if (processor != null && !processor.isEmpty()) {
            String[] processorNameList = processor.split(",");
            for (Product product : products) {
                CategoryConfigResponseDto configs = categoryConfigService.getCategoryConfigByCategory(String.valueOf(product.getCategory().getId()));
                for (String name : processorNameList) {
                    if (configs.options().containsKey("Processor") &&
                            configs.options().get("Processor").stream()
                                    .anyMatch(option -> option.name().equalsIgnoreCase(name.trim()))) {
                        processorList.add(product.getId());
                        break;
                    }

                }
            }
        }
        return processorList;
    }

    @Override
    public List<UUID> getBrand(String brand) {
        List<Product> products = productRepository.findAll();
        List<UUID> brandList = new ArrayList<>();

        if (brand != null && !brand.isEmpty()) {
            String[] brandNameList = brand.split(",");

            for (Product product : products) {
                var configs = categoryConfigService.getCategoryConfig(String.valueOf(product.getCategory().getId()));
                for (String name : brandNameList) {
                    if (configs.getCompatibleOptions().stream()
                            .anyMatch(option -> option.getAttributeOption().getBrand().equalsIgnoreCase(name.trim()))) {
                        brandList.add(product.getId());
                    }
                }


            }
        }
        return brandList;
    }


}
