package com.example.demo.service

import com.example.demo.ValuableRepo
import com.example.demo.entity.Valuable
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class ValuableService(valuableRepo: ValuableRepo) : AbsPersistentService<Valuable>(valuableRepo)
