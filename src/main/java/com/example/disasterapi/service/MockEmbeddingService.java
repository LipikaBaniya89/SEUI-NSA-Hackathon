package com.example.disasterapi.service;

import org.springframework.stereotype.Service;

@Service
public class MockEmbeddingService implements EmbeddingService {
    private static final int DIM = 256;

    @Override
    public float[] embed(String text) {
        float[] v = new float[DIM];
        if (text == null) return v;
        String[] toks = text.toLowerCase().split("[^a-z0-9]+");
        for (String t : toks) {
            if (t.isBlank()) continue;
            int h = Math.abs(t.hashCode());
            v[h % DIM] += 1.0f;
        }
        double sum = 0;
        for (float x : v) sum += x * x;
        float n = (float) Math.sqrt(sum);
        if (n > 0) for (int i = 0; i < DIM; i++) v[i] /= n;
        return v;
    }
}
